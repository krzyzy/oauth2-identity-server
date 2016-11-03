import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";

@Component({
    selector: 'user-profile',
    templateUrl: 'profile.component.html'
})
export class ProfileComponent implements OnInit {

    userData = {};

    constructor(private http: Http) {
    }

    ngOnInit() {
        this.http.get('/account/api/user')
            .map(res => res.json())
            .subscribe(
                data => this.userData = data,
                err => this.logError(err)
            );
    }

    logError(err) {
        console.error('There was an error: ' + err);
    }
}
