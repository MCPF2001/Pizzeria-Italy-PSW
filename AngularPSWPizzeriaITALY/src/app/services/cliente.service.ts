import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

export interface cliente {
  id: number;
  nome: string;
  cognome: string;
  email: string;
  telefono: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private baseUrl = 'http://localhost:8081/cliente';

  constructor(private http: HttpClient, private oauthService: OAuthService) { }

  getCurrentUser(): Observable<cliente> {
    const token = this.oauthService.getAccessToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    const url = `${this.baseUrl}/me`;
    return this.http.get<cliente>(url, { headers });
  }

  updateUser(user: cliente): Observable<cliente> {
    const token = this.oauthService.getAccessToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    const url = `${this.baseUrl}/update`;
    return this.http.put<cliente>(url, user, { headers });
  }
}
