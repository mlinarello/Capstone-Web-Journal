import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import JournalClient from "../api/journalClient";

class IndividualTaskView extends BaseClass{
    constructor() {
        super();
        this.bindClassMethods(['renderEntries', 'createEntry', 'renderJournalTitle'], this);
        this.dataStore = new DataStore();
    }

    async mount(){
        this.client = new JournalClient();
        await this.renderEntries();
        let createEntryForm = document.getElementById('createEntry-form');
        if (createEntryForm) {
            createEntryForm.addEventListener('submit', this.createEntry);
        }
        await this.renderJournalTitle();
    }

    async renderEntries() {
        let journalId = sessionStorage.getItem("journalId");
        let entries = null;
        let entriesHtml = "";
        const entryRenderArea = document.getElementById('entries');
        try{
            entries = await this.client.getAllEntriesForJournal(journalId);
            for (let i = 0; i < entries.data.length; i++) {
                const formattedTime = entries.data[i].timestamp.substring(0, 10);
                entriesHtml += `
        <div class="entry">
        <h4>${entries.data[i].title}</h4>
        <h6>${formattedTime}</h6>
        <p>${entries.data[i].body}</p>
        <button type="button" class="btn btn-outline-danger" id="deleteBtn-${entries.data[i].journalEntryId}">Delete Entry</button>
        <hr> </div>`
            }
        } catch (e){
            this.errorHandler(e);
        }
        entryRenderArea.innerHTML = entriesHtml;
        await this.loadDeleteEntryEventListeners(entries);
    }

    async loadDeleteEntryEventListeners(entries){
        for (let i = 0; i < entries.data.length; i++) {
            let button = document.getElementById(`deleteBtn-${entries.data[i].journalEntryId}`);
            button.addEventListener('click', () => {
                let journalId = entries.data[i].journalId;
                let journalEntryId = entries.data[i].journalEntryId;
                this.client.deleteJournalEntry(journalId + "--" + journalEntryId);
                setTimeout(this.renderEntries, 1200);
            })
        }
    }

    async createEntry(event) {
        event.preventDefault();
        const createEntryForm = document.getElementById('createEntry-form');
        const journalId = sessionStorage.getItem("journalId");
        const username = sessionStorage.getItem("username");
        const newEntryTitle = document.getElementById('title').value;
        const newEntryBody = document.getElementById('body').value;
        await this.client.createJournalEntry(journalId, username, newEntryTitle, newEntryBody);
        const createEntryActualForm = document.getElementById('create-entry-actualform');
        createEntryActualForm.reset();
        createEntryForm.style.display = "none";
        setTimeout(this.renderEntries, 1200);
    }

    async renderJournalTitle() {
        const titleRenderArea = document.getElementById('journal-title');
        let journalTitle = sessionStorage.getItem("journalName");
        titleRenderArea.innerHTML = `<h1>${journalTitle}</h1>`;
    }
}
const main = async () => {
    const ITV = new IndividualTaskView();
    ITV.mount();
}

window.addEventListener('DOMContentLoaded', main);