// Configurações da API
const API_BASE_URL = "/api/relatorio";

// Estado global da aplicação
let currentPage = 0;
let totalPages = 0;
let totalElements = 0;
let pageSize = 20;

// Elementos do DOM
const relatorioForm = document.getElementById('relatorioForm');
const editForm = document.getElementById('editForm');
const searchInput = document.getElementById('searchMotorista');
const relatoriosTableBody = document.getElementById('relatoriosTableBody');
const paginationInfo = document.getElementById('paginationInfo');
const pageInfo = document.getElementById('pageInfo');
const prevPageBtn = document.getElementById('prevPage');
const nextPageBtn = document.getElementById('nextPage');
const editModal = document.getElementById('editModal');
const toast = document.getElementById('toast');

// Inicialização da aplicação
document.addEventListener('DOMContentLoaded', function() {
    // Verificar se todos os elementos necessários existem
    if (!relatorioForm || !editForm || !searchInput || !relatoriosTableBody || 
        !paginationInfo || !pageInfo || !prevPageBtn || !nextPageBtn || 
        !editModal || !toast) {
        console.error('Elementos do DOM não encontrados');
        return;
    }
    
    carregarRelatorios();
    setupEventListeners();
});

// Configuração dos event listeners
function setupEventListeners() {
    // Formulário de cadastro
    relatorioForm.addEventListener('submit', handleSubmit);
    
    // Formulário de edição
    editForm.addEventListener('submit', handleEditSubmit);
    
    // Busca por motorista
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            buscarPorMotorista();
        }
    });
    
    // Fechar modal ao clicar fora
    window.addEventListener('click', function(e) {
        if (e.target === editModal) {
            fecharModal();
        }
    });
}

// Função para fazer requisições HTTP
async function apiRequest(url, options = {}) {
    try {
        console.log('Fazendo requisição para:', url, 'com opções:', options);
        
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        console.log('Status da resposta:', response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Resposta de erro:', errorText);
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        
        const data = await response.json();
        console.log('Dados da resposta:', data);
        return data;
    } catch (error) {
        console.error('Erro na requisição:', error);
        throw error;
    }
}

// Função para mostrar toast de notificação
function showToast(message, type = 'info') {
    toast.textContent = message;
    toast.className = `toast ${type}`;
    toast.classList.add('show');
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Função para carregar relatórios com paginação
async function carregarRelatorios() {
    try {
        setLoading(true);
        
        const url = `${API_BASE_URL}?page=${currentPage}&size=${pageSize}`;
        console.log('Fazendo requisição para:', url);
        
        const data = await apiRequest(url);
        console.log('Dados recebidos:', data);
        
        totalPages = data.totalPages;
        totalElements = data.totalElements;
        
        renderizarTabela(data.content);
        atualizarPaginacao();
        atualizarInfoPaginacao();
        
    } catch (error) {
        console.error('Erro detalhado ao carregar relatórios:', error);
        showToast('Erro ao carregar relatórios: ' + error.message, 'error');
    } finally {
        setLoading(false);
    }
}

// Função para renderizar a tabela
function renderizarTabela(relatorios) {
    relatoriosTableBody.innerHTML = '';
    
    if (relatorios.length === 0) {
        relatoriosTableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 40px; color: #718096;">
                    <i class="fas fa-inbox" style="font-size: 2rem; margin-bottom: 10px; display: block;"></i>
                    Nenhum relatório encontrado
                </td>
            </tr>
        `;
        return;
    }
    
    relatorios.forEach(relatorio => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${relatorio.id}</td>
            <td>${formatarData(relatorio.data)}</td>
            <td>${formatarHora(relatorio.hora)}</td>
            <td>${relatorio.motorista}</td>
            <td>${relatorio.placaVeiculo}</td>
            <td>R$ ${formatarValor(relatorio.valor)}</td>
            <td class="actions">
                <button class="btn btn-edit" onclick="editarRelatorio(${relatorio.id})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-delete" onclick="excluirRelatorio(${relatorio.id})">
                    <i class="fas fa-trash"></i> Excluir
                </button>
            </td>
        `;
        relatoriosTableBody.appendChild(row);
    });
}

// Função para buscar por motorista
async function buscarPorMotorista() {
    const motorista = searchInput.value.trim();
    
    if (!motorista) {
        showToast('Digite o nome do motorista para buscar', 'info');
        return;
    }
    
    try {
        setLoading(true);
        
        const url = `${API_BASE_URL}/buscar/${encodeURIComponent(motorista)}`;
        const relatorios = await apiRequest(url);
        
        if (relatorios.length === 0) {
            showToast('Nenhum motorista encontrado com esse nome', 'info');
            return;
        }
        
        // Resetar paginação para busca
        currentPage = 0;
        totalPages = 1;
        totalElements = relatorios.length;
        
        renderizarTabela(relatorios);
        atualizarPaginacao();
        atualizarInfoPaginacao();
        
        showToast(`${relatorios.length} relatório(s) encontrado(s)`, 'success');
        
    } catch (error) {
        if (error.message.includes('404')) {
            showToast('Motorista não encontrado', 'info');
        } else {
            showToast('Erro ao buscar motorista', 'error');
        }
        console.error('Erro ao buscar motorista:', error);
    } finally {
        setLoading(false);
    }
}

// Função para lidar com o envio do formulário
async function handleSubmit(e) {
    e.preventDefault();
    
    const formData = new FormData(relatorioForm);
    const relatorio = {
        data: formData.get('data'),
        hora: formData.get('hora'),
        motorista: formData.get('motorista'),
        placaVeiculo: formData.get('placaVeiculo'),
        valor: parseFloat(formData.get('valor'))
    };
    
    try {
        setLoading(true);
        
        await apiRequest(API_BASE_URL, {
            method: 'POST',
            body: JSON.stringify(relatorio)
        });
        
        showToast('Relatório cadastrado com sucesso!', 'success');
        relatorioForm.reset();
        carregarRelatorios();
        
    } catch (error) {
        showToast('Erro ao cadastrar relatório', 'error');
        console.error('Erro ao cadastrar relatório:', error);
    } finally {
        setLoading(false);
    }
}

// Função para editar relatório
async function editarRelatorio(id) {
    try {
        setLoading(true);
        
        // Buscar dados do relatório
        const url = `${API_BASE_URL}?page=0&size=1000`;
        const data = await apiRequest(url);
        const relatorio = data.content.find(r => r.id === id);
        
        if (!relatorio) {
            showToast('Relatório não encontrado', 'error');
            return;
        }
        
        // Preencher formulário de edição
        document.getElementById('editId').value = relatorio.id;
        document.getElementById('editData').value = relatorio.data;
        document.getElementById('editHora').value = relatorio.hora;
        document.getElementById('editMotorista').value = relatorio.motorista;
        document.getElementById('editPlacaVeiculo').value = relatorio.placaVeiculo;
        document.getElementById('editValor').value = relatorio.valor;
        
        // Abrir modal
        editModal.style.display = 'block';
        
    } catch (error) {
        showToast('Erro ao carregar dados do relatório', 'error');
        console.error('Erro ao carregar dados do relatório:', error);
    } finally {
        setLoading(false);
    }
}

// Função para lidar com a edição
async function handleEditSubmit(e) {
    e.preventDefault();
    
    const id = document.getElementById('editId').value;
    const relatorio = {
        data: document.getElementById('editData').value,
        hora: document.getElementById('editHora').value,
        motorista: document.getElementById('editMotorista').value,
        placaVeiculo: document.getElementById('editPlacaVeiculo').value,
        valor: parseFloat(document.getElementById('editValor').value)
    };
    
    try {
        setLoading(true);
        
        await apiRequest(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            body: JSON.stringify(relatorio)
        });
        
        showToast('Relatório atualizado com sucesso!', 'success');
        fecharModal();
        carregarRelatorios();
        
    } catch (error) {
        showToast('Erro ao atualizar relatório', 'error');
        console.error('Erro ao atualizar relatório:', error);
    } finally {
        setLoading(false);
    }
}

// Função para excluir relatório
async function excluirRelatorio(id) {
    if (!confirm('Tem certeza que deseja excluir este relatório?')) {
        return;
    }
    
    try {
        setLoading(true);
        
        await apiRequest(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        
        showToast('Relatório excluído com sucesso!', 'success');
        carregarRelatorios();
        
    } catch (error) {
        showToast('Erro ao excluir relatório', 'error');
        console.error('Erro ao excluir relatório:', error);
    } finally {
        setLoading(false);
    }
}

// Função para mudar página
function mudarPagina(direction) {
    const newPage = currentPage + direction;
    
    if (newPage >= 0 && newPage < totalPages) {
        currentPage = newPage;
        carregarRelatorios();
    }
}

// Função para atualizar controles de paginação
function atualizarPaginacao() {
    prevPageBtn.disabled = currentPage === 0;
    nextPageBtn.disabled = currentPage >= totalPages - 1;
}

// Função para atualizar informações de paginação
function atualizarInfoPaginacao() {
    const start = currentPage * pageSize + 1;
    const end = Math.min((currentPage + 1) * pageSize, totalElements);
    
    paginationInfo.textContent = `Mostrando ${start} a ${end} de ${totalElements} registros`;
    pageInfo.textContent = `Página ${currentPage + 1} de ${totalPages}`;
}

// Função para limpar formulário
function limparFormulario() {
    relatorioForm.reset();
}

// Função para fechar modal
function fecharModal() {
    editModal.style.display = 'none';
    editForm.reset();
}

// Função para definir estado de loading
function setLoading(loading) {
    const mainContent = document.querySelector('.main-content');
    
    if (loading) {
        mainContent.classList.add('loading');
    } else {
        mainContent.classList.remove('loading');
    }
}

// Funções de formatação
function formatarData(data) {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
}

function formatarHora(hora) {
    if (!hora) return '-';
    return hora.substring(0, 5); // Remove segundos se houver
}

function formatarValor(valor) {
    if (!valor) return '0,00';
    return valor.toLocaleString('pt-BR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

// Função para recarregar relatórios (usada no botão atualizar)
function recarregarRelatorios() {
    currentPage = 0;
    carregarRelatorios();
}

// Exportar funções para uso global
window.carregarRelatorios = carregarRelatorios;
window.buscarPorMotorista = buscarPorMotorista;
window.editarRelatorio = editarRelatorio;
window.excluirRelatorio = excluirRelatorio;
window.mudarPagina = mudarPagina;
window.limparFormulario = limparFormulario;
window.fecharModal = fecharModal;
