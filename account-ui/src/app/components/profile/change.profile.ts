import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";
import {Router} from "@angular/router";
import {ChangeProfileForm} from "./change.profile.form";

@Component({
    selector: 'user-change-profile',
    templateUrl: 'change.profile.html'
})
export class ProfileFormComponent implements OnInit {

    form = new ChangeProfileForm('', '');

    constructor(private http: Http, private router: Router) {
    }

    ngOnInit() {
        this.http.get('/account/api/user')
            .map(res => res.json())
            .subscribe(
                data => this.form = new ChangeProfileForm(data.firstName || '', data.lastName || ''),
                err => console.error('There was an error: ' + err)
            );
    }

    save($event) {
        $event.preventDefault();

        this.http.post('/account/api/user/profile', this.form)
            .map(res => res.json())
            .subscribe(
                data => this.onProfileChange(data),
                err => this.logError(err)
            );
    }

    onProfileChange(data) {
        console.info("Password has been changed" + data)
        this.router.navigate(['/user/profile'])
    }

    logError(err) {
        console.error('There was an error: ' + err);
    }
}
