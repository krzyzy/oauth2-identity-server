import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";
import {Router} from '@angular/router'
import {ChangePasswordForm} from './change.password.form'

@Component({
    selector: 'user-change-password',
    templateUrl: 'change.password.html'
})
export class ChangePasswordComponent implements OnInit{


    constructor(private http: Http, private router: Router) {
    }

    ngOnInit() {
        this.form = new ChangePasswordForm('','');
    }

    save($event) {
        $event.preventDefault();

        if (this.form.password !== this.form .confirmPassword) {
            console.warn("Passwords do not match");
            return;
        }
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
