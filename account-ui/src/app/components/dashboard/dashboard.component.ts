import {Component, OnInit} from "@angular/core";
import {ProfileService} from "../profile/profile.service";
import {DashboardService} from "./dashboard.service";

@Component({
    selector: 'my-dashboard',
    templateUrl: 'dashboard.component.html',
    providers : [ProfileService, DashboardService]
})
export class DashboardComponent implements OnInit {
    userData = {};
    clients = [];
    usersReport = {
        userCount:'N/A'
    };

    constructor(private profile: ProfileService,
                private dashboard: DashboardService) {
    }

    ngOnInit() {
        this.profile.getProfile()
            .subscribe(data => this.userData = data);
        this.dashboard.getUsersReport()
            .subscribe( data => this.usersReport = data);
    }

}
