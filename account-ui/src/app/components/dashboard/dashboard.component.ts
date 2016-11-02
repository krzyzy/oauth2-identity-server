import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";

@Component({
    selector: 'my-dashboard',
    templateUrl: 'dashboard.component.html'
})
export class DashboardComponent implements OnInit {

    constructor(private http: Http) {
        this.userData = {};
        this.clients = [];
    }

    ngOnInit() {
        this.http.get('/account/api/user')
            .map(res => res.json())
            .subscribe(
                data => this.userData = data,
                err => this.logError(err)
            );
        this.http.get('/account/api/clients')
            .map(res => res.json())
            .subscribe(
                data => this.clients = data,
                err => this.logError(err)
            );
    }

    logError(err) {
        console.error('There was an error: ' + err);
    }
}
