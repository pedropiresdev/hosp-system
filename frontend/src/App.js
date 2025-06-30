import React, { useState, useEffect } from 'react';
import PacienteForm from './components/PacienteForm';
import PacienteList from './components/PacienteList';
import pacienteService from './services/pacienteService';
import './App.css'; // Estilo global da aplicação

function App() {
  const [pacientes, setPacientes] = useState([]);
  const [editingPaciente, setEditingPaciente] = useState(null); // Para edição
  const [showForm, setShowForm] = useState(false); // Controla a visibilidade do formulário
  const [message, setMessage] = useState(''); // Mensagens de sucesso/erro

  // Função para buscar pacientes do backend
  const fetchPacientes = async () => {
    try {
      const data = await pacienteService.getAllPacientes();
      setPacientes(data);
    } catch (error) {
      setMessage(`Erro ao carregar pacientes: ${error.message}`);
      console.error('Erro ao carregar pacientes:', error);
    }
  };

  // Carrega pacientes ao montar o componente
  useEffect(() => {
    fetchPacientes();
  }, []);

  const handleCreateOrUpdatePaciente = async (pacienteData) => {
    try {
      if (pacienteData.id) {
        // Atualizar
        await pacienteService.updatePaciente(pacienteData.id, pacienteData);
        setMessage('Paciente atualizado com sucesso!');
      } else {
        // Cadastrar
        await pacienteService.createPaciente(pacienteData);
        setMessage('Paciente cadastrado com sucesso!');
      }
      setShowForm(false); // Esconde o formulário
      setEditingPaciente(null); // Limpa o paciente em edição
      fetchPacientes(); // Recarrega a lista
    } catch (error) {
      setMessage(`Erro: ${error.message}`);
      console.error('Erro ao salvar paciente:', error);
    }
  };

  const handleEdit = (paciente) => {
    setEditingPaciente(paciente);
    setShowForm(true);
  };

  const handleDelete = async (cpf) => {
    if (window.confirm(`Tem certeza que deseja remover o paciente com CPF: ${cpf}?`)) {
      try {
        await pacienteService.deletePacienteByCpf(cpf);
        setMessage('Paciente removido com sucesso!');
        fetchPacientes(); // Recarrega a lista
      } catch (error) {
        setMessage(`Erro ao remover paciente: ${error.message}`);
        console.error('Erro ao remover paciente:', error);
      }
    }
  };

  return (
      <div className="App">
        <header className="App-header">
          <h1>Sistema de Gerenciamento de Prioridade Hospitalar</h1>
        </header>
        <main>
          {message && <p className="message">{message}</p>}

          <div className="toolbar">
            <button onClick={() => { setShowForm(true); setEditingPaciente(null); }}>
              + Cadastrar Novo Paciente
            </button>
          </div>

          {showForm && (
              <div className="form-section">
                <PacienteForm
                    paciente={editingPaciente}
                    onSubmit={handleCreateOrUpdatePaciente}
                    onCancel={() => { setShowForm(false); setEditingPaciente(null); }}
                />
              </div>
          )}

          <div className="list-section">
            <PacienteList
                pacientes={pacientes}
                onEdit={handleEdit}
                onDelete={handleDelete}
            />
          </div>
        </main>
      </div>
  );
}

export default App;