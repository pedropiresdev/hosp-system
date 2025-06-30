import React, { useState } from 'react';
import './PacienteList.css'; // Estilo para a lista

function PacienteList({ pacientes, onEdit, onDelete }) {
    const [searchTerm, setSearchTerm] = useState('');

    const filteredPacientes = pacientes.filter(paciente =>
        paciente.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        paciente.cpf.includes(searchTerm)
    );

    return (
        <div className="paciente-list-container">
            <h2>Fila de Atendimento</h2>
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Buscar por nome ou CPF..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>
            {filteredPacientes.length === 0 ? (
                <p>Nenhum paciente na fila de atendimento.</p>
            ) : (
                <table className="paciente-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Idade</th>
                        <th>CPF</th>
                        <th>Prioridade</th>
                        <th>Sintomas</th>
                        <th>Sinais Vitais</th>
                        <th>Triagem</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredPacientes.map((paciente) => (
                        <tr key={paciente.id}>
                            <td>{paciente.id}</td>
                            <td>{paciente.nome}</td>
                            <td>{paciente.idade}</td>
                            <td>{paciente.cpf}</td>
                            <td>{paciente.prioridade}</td>
                            <td>{paciente.sintomas}</td>
                            <td>
                                PS: {paciente.pressaoSistolica || 'N/A'}, PD: {paciente.pressaoDiastolica || 'N/A'}<br/>
                                BPM: {paciente.batimentosCardiacos || 'N/A'}, Temp: {paciente.temperatura || 'N/A'}
                            </td>
                            <td>{new Date(paciente.dataHoraTriagem).toLocaleString()}</td>
                            <td className="actions-column">
                                <button onClick={() => onEdit(paciente)}>Editar</button>
                                <button onClick={() => onDelete(paciente.cpf)} className="delete-button">Remover</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default PacienteList;