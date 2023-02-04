import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class JournalClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'createJournal', 'getAllJournalsForUser', 'getJournal', 'updateJournalInfo', 'createJournalEntry', 'getAllEntriesForJournal'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async createJournal(username, journalName) {
        try {
            return await this.client.post(`/journal/create`, {
                "username": username, "journalName": journalName
            })
        } catch (e) {
            this.errorHandler(e)
        }
    }

    async getAllJournalsForUser(username) {
        try {
            let res = await this.client.get(`/journal/all/${username}`);
            return res.data;
        } catch (e) {
            this.errorHandler(e);
        }
    }

    async getJournal(journalId) {
        try {
            return await this.client.get(`/journal/${journalId}`);
        } catch (e) {
            this.errorHandler(e);
        }
    }

    async updateJournalInfo(username, journalId, journalName, dateCreated, isFavorite) {
        try {
            return await this.client.put(`/journal`, {
                "username": username,
                "journalId": journalId,
                "journalName": journalName,
                "dateCreated": dateCreated,
                "isFavorite": isFavorite
            })
        } catch (e) {
            this.errorHandler(e);
        }
    }

    async createJournalEntry(journalId, username, title, body) {
        try {
            let res = await this.client.post(`/journal/entry`, {
                "journalId": journalId, "username": username, "title": title, "body": body
            });
            setTimeout(() => {
                return res;
            }, 5000);
        } catch (e) {
            this.errorHandler(e);
        }
    }

    async getAllEntriesForJournal(journalId) {
        try {
            let res = await this.client.get(`/journal/entry/all/${journalId}`);
            console.log("From client class: ", res);
            return res;
        } catch (e) {
            this.errorHandler(e)
        }
    }

    async getInspirationalQuote() {
        try {
            const quote = await this.client.get('/inspiration');
            return quote.data;
        } catch (e) {
            this.errorHandler(e);
        }
    }

    async deleteJournal(journalId) {
        try{
            this.client.delete(`/journal/${journalId}`);
        } catch(e){
            this.errorHandler(e);
        }
    }

    async deleteJournalEntry(journalDeleteKey){
        try{
            this.client.delete(`/journal/entry/${journalDeleteKey}`);
        } catch (e){
            this.errorHandler(e);
        }
    }
}