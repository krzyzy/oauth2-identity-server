import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {ChangeProfileForm} from "./change.profile.form";
import {ProfileService} from "./profile.service";

@Component({
    selector: 'user-change-profile',
    templateUrl: 'change.profile.html',
    providers : [ProfileService]
})
export class ProfileFormComponent implements OnInit {

    form = new ChangeProfileForm('', '');

    constructor(private service: ProfileService, private router: Router) {
    }

    ngOnInit() {
        this.service.getProfile()
            .subscribe(
                data => this.form = new ChangeProfileForm(data.firstName || '', data.lastName || ''),
                err => console.error('There was an error: ' + err)
            );
    }

    save($event) {
        $event.preventDefault();

        this.service.updateProfile(this.form)
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
