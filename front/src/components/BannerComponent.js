import React, { Component } from 'react';
import bannerService from "../services/BannerService";
import categoryService from "../services/CategoryService";


class BannerComponent extends Component {
    constructor(props) {
        super(props)
        this.onChangeSearchName = this.onChangeSearchName.bind(this)
        this.onChangeName = this.onChangeName.bind(this)
        this.onChangePrice = this.onChangePrice.bind(this)
        this.onChangeCategory = this.onChangeCategory.bind(this)
        this.onChangeText = this.onChangeText.bind(this)

        this.setActiveBanner = this.setActiveBanner.bind(this)
        this.searchName = this.searchName.bind(this)

        this.setEditBanner = this.setEditBanner.bind(this)
        this.setCreateBanner = this.setCreateBanner.bind(this)

        this.getBanners = this.getBanners.bind(this)
        this.updateBanner = this.updateBanner.bind(this)
        this.createBanner = this.createBanner.bind(this)
        this.deleteBanner = this.deleteBanner.bind(this)

        this.cancelButton = this.cancelButton.bind(this)

        this.state = {
            banners: [],
            currentBanner: null,
            currentId: -1,
            searchName: "",

            editing: false,
            creating: false,
            onError: false,
            errorBody: [],

            categories: [],

            editingBanner: {
                name: "",
                price: 0,
                categories : [],
                text: ""
            }
        }
    }


    componentDidMount() {
        this.getBanners()

        categoryService.getCategories()
            .then( res => {
                this.setState({categories: res.data})
            })
            .catch(e => {
                console.log(e)
            })
    }

    onChangeSearchName(e) {
        const searchName = e.target.value;

        this.setState({
            searchName: searchName
        });
    }

    searchName() {
        bannerService.findBannerByName(this.state.searchName)
            .then( res => {
                this.setState({
                    banners: res.data
                });
            })
    }

    getBanners() {
        bannerService.getBanners()
            .then(res => {
                this.setState({
                    banners : res.data,
                    onError: false,
                    errorBody: []
                })
            })
            .catch(e => {
                console.log(e);
            })
    }

    createBanner() {
        bannerService.createBanner(this.state.editingBanner)
            .then( res => {
                this.setActiveBanner(res.data, res.data.id)
                this.setState({
                    creating: false,
                    editingBanner: {
                        name: "",
                        price: 0,
                        categories : [],
                        text: ""
                    }
                })
                this.getBanners()
            })
            .catch(e => {
                this.errorHandle(e)
            })
    }

    updateBanner() {
        bannerService.updateBanner(
            this.state.editingBanner.id,
            this.state.editingBanner
        )
            .then( res => {
                this.setActiveBanner(res.data, res.data.id)
                this.setState({
                    editing: false,
                    editingBanner: {
                        name: "",
                        price: 0,
                        category: {
                            name:""
                        },
                        text: ""
                    }
                })
                this.getBanners()
            })
            .catch(e => {
                this.errorHandle(e)
            })
    }

    deleteBanner() {
        bannerService.deleteBanner(this.state.currentBanner.id)
            .then(() => {
                this.setState({
                    currentBanner: null,
                    currentId: -1,
                })
                this.getBanners()
            })
            .catch(e => {
                this.errorHandle(e)
            })
    }

    errorHandle(e) {
        this.setState({
            onError: true
        })
        if (e.response && e.response.data) {
            this.setState({
                errorBody : e.response.data.violations
            })}
        setTimeout(()=>
            this.setState({
                onError:false,
                errorBody:[]
            }), 3000)
    }

    setCreateBanner() {
        this.setState({
            creating: true,
            editing: false,
        });
    }

    setEditBanner() {
        this.setState(res => ({
            editing: true,
            creating: false,
            editingBanner: {
                ...res.currentBanner
            }
        }));
    }

    setActiveBanner(banner, index) {
        this.setState({
            currentBanner: banner,
            currentId: index,
            editing: false,
            creating: false
        });
    }

    cancelButton() {
        this.setState({
            editing: false,
            creating: false,
            onError: false,
            errorBody: []
        })
    }

    onChangeName(e) {
        const name = e.target.value;

        this.setState( res => ({
            editingBanner: {
                ...res.editingBanner,
                name: name
            }
        }))
    }

    onChangePrice(e) {
        const price = e.target.value;

        this.setState( res => ({
            editingBanner: {
                ...res.editingBanner,
                price: price
            }
        }))
    }

    onChangeCategory(e) {
        const category = e.target.value;
        const selectedOptions = e.target ? Array.from(e.target.selectedOptions) : []
        console.log(category, selectedOptions)
        this.setState( res => ({
            editingBanner: {
                ...res.editingBanner,
                categories: this.state.categories.filter(c=> selectedOptions.find(o => o.value === c.id.toString()))
            }
        }))
    }

    onChangeText(e) {
        const text = e.target.value;

        this.setState( res => ({
            editingBanner: {
                ...res.editingBanner,
                text: text
            }
        }))
    }


    render() {
        const {searchName, banners, currentBanner, currentId, editing, creating, categories, editingBanner, errorBody} = this.state;
        return (
            <div className="main">
                <aside className="sidebar">
                    <header className="sidebar__header">Banners:</header>
                    <section className="sidebar__content">
                        <div className="col-sd-8">
                            <div className="sidebar__search">
                                <input
                                    type="text"
                                    className="sidebar__search-input"
                                    placeholder="Enter banner name..."
                                    value={searchName}
                                    onChange={this.onChangeSearchName}
                                />
                                <span class="sidebar__search-icon"></span>
                                <div className="input-group-append">
                                    <button
                                        className="sidebar__submit-button"
                                        type="button"
                                        onClick={this.searchName}
                                    >Find</button>
                                </div>
                            </div>
                        </div>
                        <ul className="sidebar__menu">
                            {banners && banners.map((banner, _index) => (
                                <>
                                    <li
                                        className={"sidebar__menu-item" + (banner.id === currentId ? " active" : "")}
                                        onClick={() => this.setActiveBanner(banner, banner.id)}
                                        key={banner.id}
                                    >
                                        {banner.name}
                                    </li>
                                </>
                            ))}
                        </ul>
                    </section>
                    <footer class="sidebar__footer">
                        <button className="sidebar__submit-button" onClick={() => this.setCreateBanner()}>Create banner</button>
                    </footer> 
                </aside>
                <section className="content">
                    
                    {creating ? (
                        
                        <div className="content__body">
                            <header class="content__header">
                                <span class="content__header-text">Create new banner</span>
                            </header>
                            <div className="content__form">
                                <div className="edit-form">
                                    <form>
                                        <div className="content__form-item">
                                            <label className="content__form-item-title p-2" htmlFor="name">Name:</label>
                                            <input
                                                type="text"
                                                className="content__input"
                                                id="name"
                                                placeholder="Enter banner name"
                                                onChange={this.onChangeName}
                                            />
                                        </div>
                                        <div className="content__form-item">
                                            <label className="content__form-item-title p-2" htmlFor="price">Price:</label>
                                            <input
                                                type="number"
                                                className="content__input"
                                                id="price"
                                                placeholder="Enter banner price"
                                                onChange={this.onChangePrice}
                                            />
                                        </div>
                                        <div className="content__form-item">
                                            <label className="content__form-item-title p-2" htmlFor="category">Category:</label>
                                            <div>
                                                <select class="content__select" multiple aria-label="Disabled select example"   onChange={this.onChangeCategory}>
                                                    <option value="" disabled hidden >Choose category...</option>
                                                    {categories && categories.map((category) => (
                                                        <option value={category.id} key={category.id} selected={!!editingBanner.categories.find(c=> c.name === category.name)}>{category.name}</option>
                                                    ))}
                                                </select>
                                            </div>
                                        </div>
                                        <div className="content__form-item">
                                            <label className="content__form-item-title p-2" htmlFor="text">Text:</label>
                                            <textarea
                                                rows="10"
                                                type="text"
                                                className="content__textarea"
                                                id="text"
                                                placeholder="Enter banner text"
                                                onChange={this.onChangeText}
                                            />
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <footer class="content__footer">
                                <div className="content__buttons">
                                    <button className="content__button content__button_dark" onClick={() => this.createBanner(editingBanner)}>Save</button>
                                    <button className="content__button content__button_red" onClick={() => this.cancelButton()}>Cancel</button>
                                </div>
                            </footer>
                        </div>
                    ) : (
                        <div>
                            {currentBanner ? (
                                <div>
                                    {editing ? (
                                        <div className="content__body">
                                            <header class="content__header">
                                                <span class="content__header-text">Banner, id: {currentBanner.id} editing</span>
                                            </header>
                                            <div className="content__form">
                                                <div className="edit-form">
                                                    <form>
                                                        <div className="content__form-item">
                                                            <label className="content__form-item-title p-2" htmlFor="name">Name:</label>
                                                            <input
                                                                type="text"
                                                                className="content__input"
                                                                id="name"
                                                                value={editingBanner.name}
                                                                onChange={this.onChangeName}
                                                            />
                                                        </div>
                                                        <div className="content__form-item">
                                                            <label  className="content__form-item-title p-2" htmlFor="price">Price:</label>
                                                            <input
                                                                type="number"
                                                                className="content__input"
                                                                id="price"
                                                                value={editingBanner.price}
                                                                onChange={this.onChangePrice}
                                                            />
                                                        </div>
                                                        <div className="content__form-item">
                                                            <label className="content__form-item-title p-2" htmlFor="category">Category:</label>
                                                            <div>
                                                                <select class="content__select" multiple onChange={this.onChangeCategory}>
                                                                    {categories && categories.map((category) => (
                                                                        <option value={category.id} key={category.id} selected={!!editingBanner.categories.find(c=> c.name === category.name)}>{category.name}</option>
                                                                    ))}
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div className="content__form-item">
                                                            <label className="content__form-item-title p-2" htmlFor="text">Text:</label>
                                                            <textarea
                                                                rows="10"
                                                                type="text"
                                                                className="content__textarea"
                                                                id="text"
                                                                value={editingBanner.text}
                                                                onChange={this.onChangeText}
                                                            />
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                            <footer class="content__footer">
                                                <div className="content__buttons">
                                                    <button className="content__button content__button_dark" onClick={() => this.updateBanner()}>Save</button>
                                                    {" "}
                                                    <button className="content__button content__button_red" onClick={() => this.cancelButton()}>Cancel</button>
                                                </div>
                                            </footer>
                                        </div>
                                    ) : (
                                        <div className="content__body">
                                            <header class="content__header">
                                                <span class="content__header-text">Banner, id: {currentBanner.id} editing</span>
                                            </header>
                                            <div className="content__form">
                                                <div>
                                                    <h4>Description:</h4>
                                                    <div><label><strong>Name:</strong></label>{currentBanner.name}</div>
                                                    <div><label><strong>Price:</strong></label>{currentBanner.price}</div>
                                                    <div><label><strong>Category:</strong></label> {currentBanner && currentBanner.categories.map((category) => (
                                                        <div key={category.id}>{category.name}</div>
                                                    ))}</div>
                                                    <div><label><strong>Text:</strong></label>{currentBanner.text}</div>
                                                </div>
                                            </div>
                                            <footer class="content__footer">
                                                <div className="content__buttons">
                                                    <button className="content__button content__button_dark" onClick={() => this.setEditBanner()}>Edit</button>
                                                    <button className="content__button content__button_red" onClick={() => this.deleteBanner()}>Delete</button>
                                                </div>
                                            </footer>
                                        </div>
                                    )}
                                </div>
                            ) : (
                                <div>
                                    <section class="content content_center">
                                        Choose an action.
                                    </section>
                                </div>
                            )}
                        </div>
                    )}
                    <div className = {"container d-" + (this.state.onError ? "block" : "none")} role="alert">
                        <br/>
                        {errorBody && errorBody.map((error) => (
                            <div key={error.name} className="alert alert-danger">
                                {error.message}
                            </div>
                        ))}
                    </div>
                </section>
            </div>
        );
    }

}

export default BannerComponent