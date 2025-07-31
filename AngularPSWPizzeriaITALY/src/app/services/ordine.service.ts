import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

export interface ProdottoOrdinato {
  idProdotto: number;
  nome: string;
  prezzo: number;
  quantita: number;
}

export interface Ordine {
  idOrdine: number;
  data: string;
  ora: string;
  stato: string;
  totaleOrdine: number;
  prodotti: ProdottoOrdinato[];
}

@Injectable({
  providedIn: 'root'
})
export class OrdineService {

  private baseUrl = 'http://localhost:8081/ordine';

  constructor(private httpClient: HttpClient, private oauthService: OAuthService) { }

  getMieiOrdini(): Observable<Ordine[]> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.oauthService.getAccessToken()
    });

    return this.httpClient.get<Ordine[]>(`${this.baseUrl}/miei-ordini`, { headers });
  }

  annullaOrdine(idOrdine: number, motivo: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.oauthService.getAccessToken()
    });

    const params = new HttpParams().set('motivo', motivo);

    const url = `${this.baseUrl}/${idOrdine}/annulla`;

    return this.httpClient.post(url, null, { headers, params, responseType: 'text' });
  }


}
