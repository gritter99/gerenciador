import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TransacaoService } from '../../core/services/transacao.service';
import { Transacao, TipoTransacao } from '../../models';

@Component({
  selector: 'app-transacao-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transacao-form.component.html'
})
export class TransacaoFormComponent implements OnInit {
  @Input() tipoInicial: TipoTransacao | null = null;
  @Output() cancelar = new EventEmitter<void>();
  @Output() salvar = new EventEmitter<Transacao>();

  form!: FormGroup;
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private transacaoService: TransacaoService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      tipo: [this.tipoInicial || 'RECEITA', Validators.required],
      data: [this.getToday(), Validators.required],
      valor: [null, [Validators.required, Validators.min(0.01)]],
      descricao: ['', [Validators.required, Validators.maxLength(255)]]
    });
  }

  private getToday(): string {
    return new Date().toISOString().split('T')[0];
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.transacaoService.criar(this.form.value).subscribe({
      next: (transacao) => {
        this.salvar.emit(transacao);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message || 'Erro ao criar transação');
        console.error('Create error:', err);
      }
    });
  }

  onCancelar(): void {
    this.cancelar.emit();
  }
}

