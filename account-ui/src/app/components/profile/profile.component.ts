import {Component, OnInit} from "@angular/core";
import {ProfileService} from "./profile.service";

@Component({
    selector: 'user-profile',
    templateUrl: 'profile.component.html',
    providers : [ProfileService]
})
export class ProfileComponent implements OnInit {

    userData = {};

    constructor(private service: ProfileService) {
    }

    ngOnInit() {
       this.service.getProfile()
            .subscribe(
                data => this.userData = data,
                err => this.logError(err)
            );
    }

    logError(err) {
        console.error('There was an error: ' + err);
    }
}
