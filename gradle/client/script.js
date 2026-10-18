const API_URL = 'http://localhost:8080/api/users';
const WALLET_API_URL = 'http://localhost:8080/api/wallets';
const AI_API_URL = 'http://localhost:8080/api/ai'; 

let balanceChartInstance = null;

document.getElementById('userForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value; 

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password }) 
        });

        if (response.ok) {
            const user = await response.json();
           
            localStorage.setItem('currentUserId', user.id);
            document.getElementById('userForm').reset();
            loadUsers(); 
            
            window.location.href = 'dashboard.html';
        } else {
            alert('Ошибка при сохранении пользователя');
        }
    } catch (error) {
        console.error('Ошибка соединения с сервером:', error);
        alert('Не удалось подключиться к бэкенду!');
    }
});


async function loadUsers() {
    try {
        const response = await fetch(API_URL);
        const users = await response.json();

        const userList = document.getElementById('userList');
        if (!userList) return;
        userList.innerHTML = '';

        if (users.length === 0) {
            userList.innerHTML = '<p style="color: var(--text-muted); text-align: center;">Пока нет зарегистрированных пользователей</p>';
            return;
        }

        users.forEach(user => {
            const li = document.createElement('li');
            li.className = 'user-item';
            
            li.innerHTML = `
                <div class="user-info">
                    <div class="name">👤 ${user.name}</div>
                    <div class="email">✉️ ${user.email}</div>
                </div>
                <span class="user-id">ID: ${user.id}</span>
            `;
            
            userList.appendChild(li);
        });
    } catch (error) {
        console.error('Ошибка загрузки пользователей:', error);
    }
}

const walletForm = document.getElementById('walletForm');
if (walletForm) {
    walletForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const userId = document.getElementById('walletUserId')?.value || localStorage.getItem('currentUserId');
        const currency = document.getElementById('currency').value.toUpperCase();

        try {
            const response = await fetch(`${WALLET_API_URL}?userId=${userId}&currency=${currency}`, {
                method: 'POST'
            });

            if (response.ok) {
                walletForm.reset();
                alert('Кошелек успешно создан!');
                loadAllWalletsForUser(userId); // Обновляем список и график
            } else {
                alert('Ошибка при создании кошелька');
            }
        } catch (error) {
            console.error('Ошибка соединения:', error);
            alert('Не удалось подключиться к бэкенду!');
        }
    });
}


async function loadAllWalletsForUser(userId) {
    if (!userId) return;
    try {
        const response = await fetch(`${WALLET_API_URL}/user/${userId}`);
        const wallets = await response.json();

        const walletList = document.getElementById('walletList');
        if (walletList) {
            walletList.innerHTML = '';

            if (wallets.length === 0) {
                walletList.innerHTML = '<p style="color: var(--text-muted); font-size: 0.9rem;">У этого пользователя нет кошельков</p>';
                if (balanceChartInstance) balanceChartInstance.destroy();
                return;
            }

            wallets.forEach(wallet => {
                const li = document.createElement('li');
                li.className = 'user-item';
                
                li.innerHTML = `
                    <div class="user-info">
                        <div class="name">💳 ${wallet.currency}</div>
                        <div class="email">Баланс: ${wallet.balance}</div>
                    </div>
                    <span class="user-id">Счет ID: ${wallet.id}</span>
                `;
                
                walletList.appendChild(li);
            });
        }
        
        renderChart(wallets);

    } catch (error) {
        console.error('Ошибка загрузки кошельков:', error);
    }
}


function renderChart(wallets) {
    const chartCanvas = document.getElementById('balanceChart');
    if (!chartCanvas) return;

    const ctx = chartCanvas.getContext('2d');

    const labels = wallets.map(w => w.currency);
    const data = wallets.map(w => w.balance);

    if (balanceChartInstance) {
        balanceChartInstance.destroy(); 
    }

    balanceChartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: [
                    '#6366f1',
                    '#a855f7',
                    '#ec4899',
                    '#10b981',
                    '#f59e0b'
                ],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    labels: { color: '#ffffff' }
                }
            }
        }
    });
}


async function getAiAdvice() {
    const currentUserId = localStorage.getItem('currentUserId');
    const adviceBox = document.getElementById('aiAdviceText');
    
    if (!adviceBox) return;
    if (!currentUserId) {
        adviceBox.textContent = 'Ошибка: пользователь не найден в сессии.';
        return;
    }

    adviceBox.textContent = 'ИИ анализирует ваши счета... Пожалуйста, подождите...';
    
    try {
        const res = await fetch(`${AI_API_URL}/advice/${currentUserId}`);
        if (!res.ok) throw new Error('Ошибка связи с ИИ-сервисом');
        
        const advice = await res.text();
        adviceBox.textContent = advice;
    } catch (err) {
        console.error(err);
        adviceBox.textContent = 'Не удалось получить ответ от ИИ-помощника.';
    }
}


document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
    const currentUserId = localStorage.getItem('currentUserId');
    if (currentUserId) {
        loadAllWalletsForUser(currentUserId);
    }
});