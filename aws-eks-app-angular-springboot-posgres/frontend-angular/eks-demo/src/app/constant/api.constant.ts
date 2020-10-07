import {environment} from '../../environments/environment'
export class Api {
    public static employees: string =environment.API_BASE_URL+'/api/v1/employees';
}
