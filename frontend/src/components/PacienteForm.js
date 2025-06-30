import React, { useState, useEffect } from 'react';
import './PacienteForm.css'; // Estilo para o formulário

const PrioridadeOptions = [
    "Não Urgência", "Pouca Urgência", "Muita Urgência", "Emergência"
];

function PacienteForm({ paciente, onSubmit, onCancel }) {
    const [formData, setFormData] = useState({
        id: paciente ? paciente.id : null,
        nome: paciente ? paciente.nome : '',
        idade: paciente ? paciente.idade : '',
        cpf: paciente ? paciente.cpf : '',
        sintomas: paciente ? paciente.sintomas : '',
        prioridade: paciente ? paciente.prioridade : PrioridadeOptions[0],
        pressaoSistolica: paciente ? paciente.pressaoSistolica : '',
        pressaoDiastolica: paciente ? paciente.pressaoDiastolica : '',
        batimentosCardiacos: paciente ? paciente.batimentosCardiacos : '',
        temperatura: paciente ? paciente.temperatura : '',
    });

    // Atualiza o formulário se o paciente mudar (útil para edição)
    useEffect(() => {
        if (paciente) {
            setFormData({
                id: paciente.id,
                nome: paciente.nome,
                idade: paciente.idade,
                cpf: paciente.cpf,
                sintomas: paciente.sintomas,
                prioridade: paciente.prioridade,
                pressaoSistolica: paciente.pressaoSistolica,
                pressaoDiastolica: paciente.pressaoDiastolica,
                batimentosCardiacos: paciente.batimentosCardiacos,
                temperatura: paciente.temperatura,
            });
        } else {
            // Limpa o formulário para um novo cadastro
            setFormData({
                id: null, nome: '', idade: '', cpf: '', sintomas: '', prioridade: PrioridadeOptions[0],
                pressaoSistolica: '', pressaoDiastolica: '', batimentosCardiacos: '', temperatura: ''
            });
        }
    }, [paciente]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="paciente-form">
            <h2>{paciente ? 'Editar Paciente' : 'Cadastrar Paciente'}</h2>
            <div className="form-group">
                <label htmlFor="nome">Nome:</label>
                <input type="text" id="nome" name="nome" value={formData.nome} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="idade">Idade:</label>
                <input type="number" id="idade" name="idade" value={formData.idade} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="cpf">CPF:</label>
                <input type="text" id="cpf" name="cpf" value={formData.cpf} onChange={handleChange} required />
            </div>
            <div className="form-group">
                <label htmlFor="sintomas">Sintomas:</label>
                <textarea id="sintomas" name="sintomas" value={formData.sintomas} onChange={handleChange}></textarea>
            </div>
            <div className="form-group">
                <label htmlFor="prioridade">Prioridade:</label>
                <select id="prioridade" name="prioridade" value={formData.prioridade} onChange={handleChange} required>
                    {PrioridadeOptions.map(option => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
            </div>

            <h3>Sinais Vitais</h3>
            <div className="form-group">
                <label htmlFor="pressaoSistolica">Pressão Sistólica:</label>
                <input type="number" step="0.1" id="pressaoSistolica" name="pressaoSistolica" value={formData.pressaoSistolica} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label htmlFor="pressaoDiastolica">Pressão Diastólica:</label>
                <input type="number" step="0.1" id="pressaoDiastolica" name="pressaoDiastolica" value={formData.pressaoDiastolica} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label htmlFor="batimentosCardiacos">Batimentos Cardíacos:</label>
                <input type="number" step="0.1" id="batimentosCardiacos" name="batimentosCardiacos" value={formData.batimentosCardiacos} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label htmlFor="temperatura">Temperatura:</label>
                <input type="number" step="0.1" id="temperatura" name="temperatura" value={formData.temperatura} onChange={handleChange} />
            </div>

            <div className="form-actions">
                <button type="submit">{paciente ? 'Atualizar' : 'Cadastrar'}</button>
                {onCancel && <button type="button" onClick={onCancel} className="cancel-button">Cancelar</button>}
            </div>
        </form>
    );
}

export default PacienteForm;