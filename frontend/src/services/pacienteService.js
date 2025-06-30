const API_BASE_URL = 'http://localhost:8080/api/pacientes'; // URL do seu backend Spring Boot

const pacienteService = {
    // --- Cadastrar Paciente ---
    createPaciente: async (pacienteData) => {
        try {
            const response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(pacienteData),
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Erro ao cadastrar paciente.');
            }
            return await response.json();
        } catch (error) {
            console.error('Erro na requisição de cadastro:', error);
            throw error;
        }
    },

    // --- Listar Pacientes ---
    getAllPacientes: async () => {
        try {
            const response = await fetch(API_BASE_URL);
            if (!response.ok) {
                throw new Error('Erro ao buscar pacientes.');
            }
            return await response.json();
        } catch (error) {
            console.error('Erro na requisição de listagem:', error);
            throw error;
        }
    },

    // --- Buscar Paciente por CPF ---
    getPacienteByCpf: async (cpf) => {
        try {
            const response = await fetch(`${API_BASE_URL}/cpf/${cpf}`);
            if (!response.ok) {
                if (response.status === 404) {
                    return null; // Paciente não encontrado
                }
                throw new Error('Erro ao buscar paciente por CPF.');
            }
            return await response.json();
        } catch (error) {
            console.error('Erro na requisição de busca por CPF:', error);
            throw error;
        }
    },

    // --- Atualizar Paciente ---
    updatePaciente: async (id, pacienteData) => {
        try {
            const response = await fetch(`${API_BASE_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(pacienteData),
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Erro ao atualizar paciente.');
            }
            return await response.json();
        } catch (error) {
            console.error('Erro na requisição de atualização:', error);
            throw error;
        }
    },

    // --- Remover Paciente por CPF ---
    deletePacienteByCpf: async (cpf) => {
        try {
            const response = await fetch(`${API_BASE_URL}/cpf/${cpf}`, {
                method: 'DELETE',
            });
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Paciente não encontrado para exclusão.');
                }
                throw new Error('Erro ao remover paciente.');
            }
            // Não há corpo de resposta para 204 No Content
        } catch (error) {
            console.error('Erro na requisição de exclusão:', error);
            throw error;
        }
    },
};

export default pacienteService;