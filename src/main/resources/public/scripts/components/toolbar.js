export function initializeToolbar() {
    const toolbarHTML = `
        <nav class="navbar navbar-expand-lg navbar-light bg-light px-5">
            <a class="navbar-brand" href="../index.html">EStore Management</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nev-item">
                        <a class="nav-link" href="../pages/references.html?model=Shop&extendedBy=ElectroItem">
                            Добавить товары в магазин
                        </a>
                    </li>
                    <li class="nev-item">
                        <a class="nav-link" href="../pages/references.html?model=Employee&extendedBy=ElectroType">
                            Настроить назначения сотрудников
                        </a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                            Реестры
                        </a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="../pages/viewdata.html?model=Employee">Сотрудники</a>
                            <a class="dropdown-item" href="../pages/viewdata.html?model=ElectroItem">Товары</a>
                            <a class="dropdown-item" href="../pages/viewdata.html?model=Purchase">Продажи</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                            Справочники
                        </a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="../pages/viewdata.html?model=Shop">Магазины</a>
                            <a class="dropdown-item" href="../pages/viewdata.html?model=ElectroType">Типы электроники</a>
                            <a class="dropdown-item" href="../pages/viewdata.html?model=PurchaseType">Типы оплаты</a>
                            <a class="dropdown-item" href="../pages/viewdata.html?model=PositionType">Типы должностей</a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
    `;
    document.getElementById('toolbar').innerHTML = toolbarHTML;
}
