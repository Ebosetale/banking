(function(){
    "use strict";
    document.addEventListener("DOMContentLoaded", () => {
        const baseUrl = `${window.location.href}api`;
        console.log(baseUrl)

        // load all account if available
        getAjax(`${baseUrl}/accounts`)
        .then(accounts => {
            const tableBody = document.getElementById("a-info-table-body");
            tableBody.innerHTML = generateAccountInfoTableBody(accounts);
        })
        .catch(err => console.error(err));

        // Generate account numbers handler
        let _form = document.getElementById("f-g-acounts");
        _form.addEventListener("submit", async (e) => {
            e.preventDefault();
            postAjax(`${baseUrl}/accounts`, await extractFormData(_form))
                .then(response => {
                    const tableBody = document.getElementById("a-info-table-body");
                    tableBody.innerHTML = generateAccountInfoTableBody(response);
                })
                .catch(err => {
                    console.error("Error:",err)
                });
        });


        // credit account event handler
        let _creditForm = document.getElementById("f-credit");
        _creditForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            postAjax(`${baseUrl}/transactions/creditAccount`, await extractFormData(_creditForm))
                .then(response => {
                    const tableBody = document.getElementById("a-info-table-body");
                    tableBody.innerHTML = generateAccountInfoTableBody(response.accounts);

                    const trxnInfoTableBody = document.getElementById("t-info-table-body");
                    trxnInfoTableBody.innerHTML = generateTxnInfoTableBody(response.transactions);
                })
                .catch(err => {
                    console.error("Error:",err)
                });
        });


        // debit account event handler
        const _debitForm = document.getElementById("f-debit");
        _debitForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const fData = await extractFormData(_debitForm);
            const req = {
                accountNumber : fData.dAccountNumber,
                amount: fData.dAmount
            }
            postAjax(`${baseUrl}/transactions/debitAccount`, req)
                .then(response => {
                    const tableBody = document.getElementById("a-info-table-body");
                    tableBody.innerHTML = generateAccountInfoTableBody(response.accounts);

                    const trxnInfoTableBody = document.getElementById("t-info-table-body");
                    trxnInfoTableBody.innerHTML = generateTxnInfoTableBody(response.transactions);
                })
                .catch(err => {
                    console.error("Error:", err)
                });

        });

        // Get transactions 
        const _tForm = document.getElementById("t-form");
        _tForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const fData = await extractFormData(_tForm);
            getAjax(`${baseUrl}/transactions/${fData.tAccountNumber}`)
            .then(res => {
                const trxnInfoTableBody = document.getElementById("t-info-table-body");
                trxnInfoTableBody.innerHTML = generateTxnInfoTableBody(res);
            })
            .catch(err => {

            });
        });

    });

    const extractFormData = async (form) => {
        let rawData = new FormData(form);
        let data = {};
        for(let pair of rawData.entries()) {
            data[pair[0]] = pair[1]; 
        }
        return data;
    }

    const generateAccountInfoTableBody = (response) => {
        let domRow = "";
        for (let index = 0; index < response.length; index++) {
            const row = response[index];
            domRow += `<tr>
                            <td>${row.accountName}</td>
                            <td>${row.phoneNumber}</td>
                            <td>${row.accountNumber}</td>
                            <td>${row.availableBalance}</td>
                        </tr>`
            
        }
        return domRow;
    }

    const generateTxnInfoTableBody = (txns) => {
        let domRow = "";
        for (let index = 0; index < txns.length; index++) {
            const row = txns[index];
            domRow += `<tr>
                            <td>${row.accountName}</td>
                            <td>${row.accountNumber}</td>
                            <td>${row.amount}</td>
                            <td>${row.statusMessage}</td>
                        </tr>`
            
        }
        return domRow;

    }

    const postAjax = async (path, data) => {
        const res = await fetch(path, {
            method: 'POST',
            cache: 'no-cache',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if(!res.ok || (res.status !== 201 && res.status !== 200)){
            throw new Error(res.statusText)
        }
        const response = res.json();
        return response;
    }

    const getAjax = async (path) => {
        const res = await fetch(path, {
            method: 'GET',
            cache: 'no-cache',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if(!res.ok || res.status !== 200){
            throw new Error(res.statusText)
        }
        const response = res.json();
        return response;
    }
})();


