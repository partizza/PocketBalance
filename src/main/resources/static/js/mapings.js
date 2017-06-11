
function getTransactionTypeText(key){
    var map = {
        'CHILDREN':'Children',
        'ENTERTAINMENT':'Entertainment',
        'FOOD':'Food',
        'GIFTS':'Gifts, Donations',
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

    return map[key];
}
