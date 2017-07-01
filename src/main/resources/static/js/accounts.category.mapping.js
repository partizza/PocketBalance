var accountCategoryMap = {
    'ASSET':'Assets',
    'LIABILITY':'Liabilities',
    'EQUITY':'Equity',
    'PROFIT':'Incomes',
    'LOSS':'Expenses',
}


function getAccountCategoryText(key){
    return accountCategoryMap[key];
}

function getAccountCategoryKey(text){
    return Object.keys(accountCategoryMap).find(key => accountCategoryMap[key] === text);
}
