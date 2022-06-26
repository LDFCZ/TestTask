import React, { Component } from 'react';
import BidService from "../services/BidService";


class BidComponent extends Component {
    constructor(props) {
        super(props)
        this.getBannerView = this.getBannerView.bind(this)
        this.onChangeCategory = this.onChangeCategory.bind(this)

        this.state = {
            bannerText: "",
            categoryReqName: "",
            categories: [],
            selectedCategories:[]
        }
    }

    onChangeCategory(e) {
        const category = e.target.value;
        const selectedOptions = e.target ? Array.from(e.target.selectedOptions) : []
        console.log(category, selectedOptions)
        this.setState( res => ({
                selectedCategories: this.state.categories.filter(c=> selectedOptions.find(o => o.value === c.requestId))
        }))
    }

    componentDidMount() {

        BidService.getCategories()
            .then( res => {
                this.setState({categories: res.data})
            })
            .catch(e => {
                console.log(e)
            })
    }

    getBannerView() {
        BidService.getBannerByCategories(this.state.selectedCategories.map(c=> c.requestId))
        .then( res => {
            this.setState({
                bannerText: res.data
            })
        })
        .catch(e => {
            console.log(e)
        })
    }

    render() {
        const {bannerText, categories, selectedCategories} = this.state;
        return (
            <div className="main">
                <div className="col-md-3">
                    <form>
                        <div className="form-group">
                            <label className="content__header-text" htmlFor="requestId"><strong>Choose categories</strong></label>
                            <div>
                                <select className="content__select" multiple aria-label="Disabled select example"
                                        onChange={this.onChangeCategory}>
                                    <option value="" disabled hidden>Choose categories</option>
                                    {categories && categories.map((category) => (
                                        <option value={category.requestId} key={category.id}
                                                selected={!!selectedCategories.find(c => c.id)}>{category.name}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </form>
                    <button className="sidebar__submit-button" onClick={this.getBannerView}>Get banner</button>
                </div>
                <div className="col-md-6">
                    <div>
                        <label className="content__header-text">
                            <strong>Banner Text:</strong>
                        </label>{" "}
                        <div>
                            {bannerText}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default BidComponent;