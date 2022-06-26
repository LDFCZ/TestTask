import axios from 'axios';

const BANNER_BID_URL = "http://localhost:8080/bid";

class BidService {

    getBannerByCategories(categories) {
        console.log(categories)
        return axios.get(BANNER_BID_URL + "?" + categories.map(c => "cat="+ c).join("&"));
    }
}

export default new BidService()