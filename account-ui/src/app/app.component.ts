import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import 'rxjs/add/operator/filter';
import {Http, Response, Headers, RequestOptions} from "@angular/http";

@Component({
    selector: 'app',

    templateUrl: 'app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    title = 'Solidify Account';

    constructor(
        private router: Router,
        private http: Http
    ) {
    }


    ngOnInit() {
    }

    logout(event: Event){
        event.stopPropagation();

        this.http.post("../logout", null, {})
            .subscribe(
                date => window.location.href = "../login",
                error => console.log("Logout failed")
            )
    }
}
