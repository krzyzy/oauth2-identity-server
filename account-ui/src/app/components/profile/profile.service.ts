import {Http} from "@angular/http";
import {Injectable} from "@angular/core";

@Injectable()
export class ProfileService {

    constructor(private http: Http) {
    }

    getProfile() {
        return this.http.get('/account/api/me')
            .map(res => res.json());
    }

    updateProfile(form) {
        return this.http.post('/account/view/user/profile', form)
            .map(res => res.json());
    }

}