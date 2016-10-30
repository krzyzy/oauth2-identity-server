import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {Router} from '@angular/router'

@Component({
    selector: 'user-change-password',
    templateUrl: 'change.password.html'
})
export class ChangePasswordComponent {

    constructor(private http: Http, private router: Router) {
        this.form = {
            password: "",
            confirmPassword: ""
        };
    }

    save($event) {
        $event.preventDefault();
        if (!this.form .password || !this.form .confirmPassword) {
            console.warn("Password is required");
            return;
        }
        if (this.form.password !== this.form .confirmPassword) {
            console.warn("Passwords do not match");
            return;
        }
        console.log(this.form);
        this.http.post('/account/api/user/password', this.form )
            .map(res => res.json())
            .subscribe(
                data => this.onPasswordChange(data),
                err => this.logError(err)
            );
    }

    onPasswordChange(data) {
        console.info("Password has been changed" + data)
        this.router.navigate(['/user/profile'])
    }

    logError(err) {
        console.error('There was an error: ' + err);
    }
}
