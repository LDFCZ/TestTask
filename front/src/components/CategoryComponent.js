import React, { Component } from 'react';
import CategoryService from '../services/CategoryService';

class CategoryComponent extends Component {
    constructor(props) {
        super(props)
        this.onChangeSearchName = this.onChangeSearchName.bind(this)
        this.onChangeName = this.onChangeName.bind(this)
        this.onChangeReqName = this.onChangeReqName.bind(this)

        this.setActiveCategory = this.setActiveCategory.bind(this)
        this.searchName = this.searchName.bind(this)

        this.setEditCategory = this.setEditCategory.bind(this)
        this.setCreateCategory = this.setCreateCategory.bind(this)

        this.getCategories = this.getCategories.bind(this)
        this.updateCategory = this.updateCategory.bind(this)
        this.createCategory = this.createCategory.bind(this)
        this.deleteCategory = this.deleteCategory.bind(this)

        this.cancelButton = this.cancelButton.bind(this)

        this.state = {
            categories: [],

            currentCategory: null,
            currentId: -1,
            searchName: "",

            editing: false,
            creating: false,
            onError: false,
            errorBody: [],

            editingCategory: {
                name: "",
                requestId: "",
            }
        }
    }

    componentDidMount() {
        this.getCategories()
    }

    getCategories() {
        CategoryService.getCategories()
            .then(res => {
                this.setState({
                    categories: res.data,
                    onError: false,
                    errorBody: []
                })
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
        CategoryService.findCategoryByName(this.state.searchName)
            .then( res => {
                this.setState({
                    categories: res.data
                });
            })
    }

    createCategory() {
        CategoryService.createCategory(this.state.editingCategory)
            .then( res => {
                this.setActiveCategory(res.data, res.data.id)
                this.setState({
                    creating: false,
                    editingCategory: {
                        name: "",
                        requestId: ""
                    }
                })
                this.getCategories()
            })
            .catch(e => {
                this.errorHandle(e);
            })
    }

    updateCategory() {
        CategoryService.updateCategory(
            this.state.editingCategory,
            this.state.currentCategory.id,

        )
            .then( res => {
                this.setActiveCategory(res.data, res.data.id)
                this.setState({
                    ...this.state,
                    editing: false,
                    editingCategory: {
                        ...this.state.editingCategory,
                        name: "",
                        requestId: ""
                    }
                })
                console.log()
                this.getCategories()
            })
            .catch(e => {
                console.log(e)
                this.errorHandle(e);
            })
    }

    deleteCategory() {
        console.log(this.state.currentCategory.id)
        CategoryService.deleteCategory(this.state.currentCategory.id)
            .then(() => {
                this.setState({
                    currentCategory: null,
                    currentId: -1,
                })
                this.getCategories()
            })
            .catch(e => {
                this.errorHandle(e);
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
    }

    setCreateCategory() {
        this.setState({
            creating: true,
            editing: false,
        });
    }

    setEditCategory() {
        this.setState(res => ({
            editing: true,
            creating: false,
            editingCategory: {
                ...res.currentCategory
            }
        }));
    }

    setActiveCategory(category, id) {
        this.setState({
            currentCategory: category,
            currentId: id,
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
            editingCategory: {
                ...res.editingCategory,
                name: name
            }
        }))
    }

    onChangeReqName(e) {
        const requestId = e.target.value;

        this.setState( res => ({
            editingCategory: {
                ...res.editingCategory,
                requestId: requestId
            }
        }))
    }

    render() {
        const {searchName, categories, currentCategory, currentId, editing, creating, editingCategory, errorBody} = this.state;
        return (
            <div className="main">
                <aside className="sidebar">
                    <header className="sidebar__header">Categories:</header>
                    <section className="sidebar__content">
                        <div className="col-sd-8">
                            <div className="sidebar__search">
                                <input
                                    type="text"
                                    className="sidebar__search-input"
                                    placeholder="Enter category name..."
                                    value={searchName}
                                    onChange={this.onChangeSearchName}
                                />
                                <span className="sidebar__search-icon"></span>
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
                            {categories && categories.map((category, _index) => (
                                <li
                                    className={"sidebar__menu-item" + (category.id === currentId ? " active" : "")}
                                    onClick={() => this.setActiveCategory(category, category.id)}
                                    key={category.id}
                                >
                                    {category.name}
                                </li>
                            ))}
                        </ul>
                    </section>
                    <footer className="sidebar__footer">
                        <button className="sidebar__submit-button" onClick={() => this.setCreateCategory()}>Create category</button>
                    </footer> 
                </aside>
                <section className="content">
                    {creating ? (
                        <div className="content__body">
                            <header className="content__header">
                                <span className="content__header-text">Create new category</span>
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
                                                placeholder="Enter category name"
                                                onChange={this.onChangeName}
                                            />
                                        </div>
                                        <div className="content__form-item">
                                            <label className="content__form-item-title p-2" htmlFor="request id">Request ID:</label>
                                            <input
                                                type="text"
                                                className="content__input"
                                                id="request id"
                                                placeholder="Enter short request id"
                                                onChange={this.onChangeReqName}
                                            />
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <footer className="content__footer">
                                <div className="content__buttons">
                                    <button className="content__button content__button_dark" onClick={() => this.createCategory()}>Save</button>
                                    <button className="content__button content__button_red" onClick={() => this.cancelButton()}>Cancel</button>
                                </div>
                            </footer>
                        </div>
                    ) : (
                        <div>
                            {currentCategory ? (
                                <div>
                                    {editing ? (
                                        <div className="content__body">
                                            <header className="content__header">
                                                <span className="content__header-text">Category, id: {currentCategory.id} editing</span>
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
                                                                value={editingCategory.name}
                                                                onChange={this.onChangeName}
                                                            />
                                                        </div>
                                                        <div className="content__form-item">
                                                            <label className="content__form-item-title p-2" htmlFor="requestId">Request ID:</label>
                                                            <input
                                                                type="text"
                                                                className="content__input"
                                                                id="price"
                                                                value={editingCategory.requestId}
                                                                onChange={this.onChangeReqName}
                                                            />
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                            <footer className="content__footer">
                                                <div className="content__buttons">
                                                    <button className="content__button content__button_dark" onClick={() => this.updateCategory()}>Save</button>
                                                    {" "}
                                                    <button className="content__button content__button_red" onClick={() => this.cancelButton()}>Cancel</button>
                                                </div>
                                            </footer>
                                        </div>
                                    ) : (
                                        <div className="content__body">
                                            <header className="content__header">
                                                <span className="content__header-text">Category, id: {currentCategory.id} editing</span>
                                            </header>
                                            <div className="content__form">
                                                <div>
                                                        <h4>Description:</h4>
                                                        <div><label>Name: <input type="text" readOnly value={currentCategory.name}/></label></div>
                                                        <div><label>Request ID: <input type="text" readOnly value={currentCategory.requestId}/></label></div>
                                                </div>
                                            </div>
                                            <footer className="content__footer">
                                                <div className="content__buttons">
                                                    <button className="content__button content__button_dark" onClick={() => this.setEditCategory()}>Edit</button>
                                                    <button className="content__button content__button_red" onClick={() => this.deleteCategory()}>Delete</button>
                                                </div>
                                            </footer>
                                        </div>
                                    )}
                                </div>
                            ) : (
                                <div>
                                    <section className="content content_center">
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


export default CategoryComponent;