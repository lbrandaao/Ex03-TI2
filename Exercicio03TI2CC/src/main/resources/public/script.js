function requestProductTable(orderby) {
    let xhr = new XMLHttpRequest();
    xhr.onload = addTable;
    xhr.onerror = error;
    xhr.open('GET', `/produto/list/${orderby}`);
    xhr.send();
}

function addTable() {
	let HTMLcontent = this.responseText;
	document.getElementById("product-table").innerHTML += HTMLcontent;
	
	let detail_items = document.querySelectorAll(".detail-icon");
    detail_items.forEach((i) => {
        i.addEventListener('click', detailItem);
    })

    let edit_items = document.querySelectorAll(".edit-icon");
    edit_items.forEach((i) => {
        i.addEventListener('click', editItem);
    })

    let remove_items = document.querySelectorAll(".remove-icon");
    remove_items.forEach((i) => {
        i.addEventListener('click', removeItem);
    })
}

function error (err) {
    console.log('Erro:', err);
}

function detailItem() {
    let itemID = this.getAttribute("alt");
    let xhr = new XMLHttpRequest();
    xhr.onload = showItem;
    xhr.onerror = error;
    xhr.open('GET', `/produto/${itemID}`);
    xhr.send();
}

function showItem() {
    alert(this.responseText);
}

function editItem() {
    let itemID = this.getAttribute("alt");
    let xhr = new XMLHttpRequest();
    xhr.onload = getItemToUpdate;
    xhr.onerror = error;
    xhr.open('GET', `/produto/update/${itemID}`);
    xhr.send();
}

function getItemToUpdate() {
    let HTMLcontent = this.responseText;
    document.getElementById("oneProduct").innerHTML = HTMLcontent;
}

function removeItem() {
    let itemID = this.getAttribute("alt");
    let xhr = new XMLHttpRequest();
    xhr.onload = notification;
    xhr.onerror = error;
    xhr.open('GET', `/produto/delete/${itemID}`);
    xhr.send();
}

function notification() {
    alert("Item removido com sucesso.");
}

// Settings
onload = () => {
	requestProductTable(1);
}