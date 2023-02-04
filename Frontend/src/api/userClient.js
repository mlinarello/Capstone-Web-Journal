import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class UserClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'createAccount', 'login'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }


    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async createAccount(name, username, email, password) {
        try {
            return await this.client.post(`/user/create`, {
                "username": username,
                "name": name,
                "password": password,
                "email": email
            });
        } catch (error) {
            this.handleError();
        }
    }

    async login(username, password) {
        try {
            let res = await this.client.post(`/user/login`, {
                "username": username,
                "password": password
            });
            console.log("From userClient: ", res.data);
            return res.data;
        } catch (error) {
            this.handleError();
        }
    }

    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}