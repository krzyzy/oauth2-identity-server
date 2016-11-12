import {Http} from "@angular/http";
import {Injectable} from "@angular/core";

@Injectable()
export class DashboardService {

    constructor(private http: Http) {
    }

    getUsersReport() {
        return this.http.get('/account/api/dashboard/users/report')
            .map(res => res.json());
    }

    getClients(){
        return this.http.get('/account/api/clients')
            .map(res => res.json());
    }
}