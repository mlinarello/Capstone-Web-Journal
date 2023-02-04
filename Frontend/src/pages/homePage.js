import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import JournalClient from "../api/journalClient";

class HomePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderJournals', 'createJournal', 'loadInspirationalQuote',
        "addIndividualJournalViewEventListeners"], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.client = new JournalClient();
        await this.renderJournals();
        let createJournalForm = document.getElementById('create-journal-form');
        if (createJournalForm) {
            createJournalForm.addEventListener('submit', this.createJournal);
        }
        await this.loadInspirationalQuote();
    }


    async renderJournals() {
        let username = sessionStorage.getItem("username");
        let listItems = "";
        let journals = null;
        try {
            journals = await this.client.getAllJournalsForUser(username);
            for (let i = 0; i < journals.length; i++) {
                listItems += `<div class="col-sm-6">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${journals[i].journalName}</h5>
                <a href="individualJournalView.html" class="btn btn-primary" id='ijv-${journals[i].journalId}'>Go to journal!</a>
            </div>
             <button type="button" class="btn btn-outline-danger" id="deleteBtn-${journals[i].journalId}">Delete Journal</button>
        </div>
    </div>`
            }
        } catch (error) {
            alert("If you're seeing me, there was an issue rendering tasks :(")
        }
        let welcomeMessage = document.getElementById("welcome");
        if (username == null) {
            welcomeMessage.innerHTML = "<h2>You don't appear to be logged in, try logging in using the button above!</h2>";
        } else {
            document.getElementById("welcome").innerHTML = `<h2>Welcome ${username}!</h2>
                           <button id="logout-button" class="btn btn-dark"><h5>Logout</h5></button>`;
            try {
                document.getElementById("logout-button").addEventListener('click', () => {
                    sessionStorage.clear();
                    window.location.replace("/index.html");
                });
            } catch (event) {
                this.errorHandler(event);
            }

        }
        let renderArea = document.getElementById("journals");
        renderArea.innerHTML = listItems;
        await this.addIndividualJournalViewEventListeners(journals);
        await this.addDeleteJournalEventListeners(journals);
    }

    async addIndividualJournalViewEventListeners(journalInfo){
        for (let i = 0; i < journalInfo.length; i++) {
            let button = document.getElementById("ijv-" + journalInfo[i].journalId);
            button.addEventListener('click', function(){
                sessionStorage.setItem("journalName", journalInfo[i].journalName);
                sessionStorage.setItem("journalId", journalInfo[i].journalId);
                window.location.replace("/individualTaskView.html");
            });
        }
    }

    async addDeleteJournalEventListeners(journalInfo){
        for (let i = 0; i < journalInfo.length; i++) {
            let button = document.getElementById('deleteBtn-' + journalInfo[i].journalId);
            button.addEventListener('click', () => {
                this.client.deleteJournal(journalInfo[i].journalId);
                setTimeout(() => {
                    this.renderJournals();
                }, 1000);
            })
        }
    }

    async loadInspirationalQuote(){
        const quoteRenderArea = document.getElementById('quote-render-area');
        const quote = await this.client.getInspirationalQuote();

        quoteRenderArea.innerHTML = `<p>${quote.body}</p>
            <footer class="blockquote-footer">${quote.author}</footer>`
    }

    async createJournal(event) {
        event.preventDefault();
        const newJournalTitle = document.getElementById('title').value;
        const username = sessionStorage.getItem("username");
        await this.client.createJournal(username, newJournalTitle);
        const form = document.getElementById('create-entry-actualform');
        form.reset();
        document.getElementById('create-journal-form').style.display = "none";
        setTimeout(() => {
            this.renderJournals();
        }, 1000);
    }
}

const main = async () => {
    const homePage = new HomePage();
    homePage.mount();
}
window.addEventListener('DOMContentLoaded', main);

