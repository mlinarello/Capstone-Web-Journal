import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserClient from "../api/userClient";

class LoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['login'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        let loginForm = document.getElementById('login-input');
        if (loginForm) {
            loginForm.addEventListener('submit', this.login);
        }
        this.client = new UserClient();
    }

    async login(event) {
        event.preventDefault();
        let loginForm = document.getElementById('login-input');
        let username = document.getElementById('login-input-username').value;
        let password = document.getElementById('login-input-password').value;

        const login = this.client.login(username, password);
        let responseUsername = "";
        login.then((res) => {
            responseUsername = res;
        });
        if (login) {
            sessionStorage.setItem("username", username);
            window.location.replace("homePage.html");
        } else {
            reject("Didn't work");
        }

        this.dataStore.set("user", responseUsername);
        loginForm.reset();
    }
}

const main = async () => {
    const login = new LoginPage();
    login.mount();
}
window.addEventListener('DOMContentLoaded', main);