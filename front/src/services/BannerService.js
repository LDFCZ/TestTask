import axios from 'axios';
import authHeader from "../security/services/authHeader";

const BANNER_API_BASE_URL ="http://localhost:8080/banners";

class BannerService{

    getBanners() {
        return axios.get(BANNER_API_BASE_URL, { headers: authHeader() });
    }

    createBanner(banner) {
        console.log(banner);
        return axios.post(BANNER_API_BASE_URL, banner, { headers: authHeader() });
    }

    getBannerById(bannerId) {
        return axios.get(BANNER_API_BASE_URL + '/' + bannerId, { headers: authHeader() });
    }

    updateBanner(bannerId, banner) {
        return axios.put(BANNER_API_BASE_URL + '/' + bannerId, banner, { headers: authHeader() });
    }

    deleteBanner(bannerId) {
        return axios.delete(BANNER_API_BASE_URL + '/' + bannerId, { headers: authHeader() });
    }

    findBannerByName(name) {
        return axios.get(BANNER_API_BASE_URL + '?name=' + name, { headers: authHeader() });
    }

}

export default new BannerService()