import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";
import {Router} from "@angular/router";
import {ChangePasswordForm} from "./change.password.form";

@Component({
    selector: 'user-change-password',
    templateUrl: 'change.password.html'
})
export class ChangePasswordComponent implements OnInit {

    form = {};
    constructor(private http: Http, private router: Router) {
    }

    ngOnInit() {
        this.form = new ChangePasswordForm('', '');
    }

    save($event) {
        $event.preventDefault();
        this.http.post('/account/view/user/password', this.form)
            .map(res => res.json())
            .subscribe(
                data => this.onPasswordChange(data),
                err => this.logError(err)
            );
    }

    arePasswordsEqual(password, confirmPassword) {
        if (!password.valid || !confirmPassword.valid) {
            return true;
        }
        return password.model === confirmPassword.model;
    }

    onPasswordChange(data) {
        console.info("Password has been changed" + data)
        this.router.navigate(['/user/profile'])
    }

    logError(err) {
        console.error('There was an error: ' + err);
    }
}
