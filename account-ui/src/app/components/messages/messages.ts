import {Component, OnInit} from "@angular/core";

@Component({
    selector: 'messages',
    templateUrl: 'messages.html',
})
export class MessageComponent implements OnInit {

    messages = null;

    ngOnInit(): void {
    }

    info(message) {
        this.messages = message;
    }
    error(message) {
        this.messages = message;
    }
}