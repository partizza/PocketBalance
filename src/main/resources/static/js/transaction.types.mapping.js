
var map = {
    'CHILDREN':'Children',
    'ENTERTAINMENT':'Entertainment',
    'FOOD':'Food',
    'GIFTS':'Gift & Donation',
    'HOUSING':'Housing',
    'INCOME':'Income',
    'INSURANCE':'Insurance',
    'LOAN':'Loans',
    'OTHER':'Other',
    'PERSONAL_CARE':'Personal care',
    'SAVINGS':'Savings',
    'TAXES':'Taxes',
    'TRANSPORT':'Transport'
}


function getTransactionTypeText(key){
    return map[key];
}

function getTransactionTypeKey(text){
    return Object.keys(map).find(key => map[key] === text);
}
