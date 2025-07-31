import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OAuthService } from 'angular-oauth2-oidc';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-prodotto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './prodotto.component.html',
  styleUrls: ['./prodotto.component.css']
})
export class ProdottoComponent {
  prodotto = {
    nome: '',
    prezzo: 0,
    quantita: 0,
    TipoProdotto: '',
    disponibilita: false
  };

  successMessage: string = '';
  errorMessage: string = '';

  constructor(private httpClient: HttpClient, private oauthService: OAuthService) { }

  aggiungiProdotto() {
    const token = this.oauthService.getAccessToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    this.httpClient.post<{ message: string }>('http://localhost:8081/aggiungi', this.prodotto, { headers })
      .subscribe(
        response => {
          this.successMessage = response.message;
          this.errorMessage = '';
          this.prodotto = {
            nome: '',
            prezzo: 0,
            TipoProdotto: '',
            quantita: 0,
            disponibilita: false
          };
        },
        error => {
          console.error('Errore nell\'aggiunta del prodotto:', error);
          this.errorMessage = error.error.error || 'Errore nell\'aggiunta del prodotto.';
          this.successMessage = '';
        }
      );
  }
}
