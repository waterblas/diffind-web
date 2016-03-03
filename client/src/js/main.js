if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    };
}
var check_fields = function(q){
    var query = $("input[name="+q+"]").val();
    query = query.trim();
    return query.length != 0;
};
var search_query = function(q){
    if (check_fields(q)){
        window.location.href="s?q=" + $("input[name="+q+"]").val().trim();
    }
    return false
};
function enterIn(evt, q){
    var evted = evt? evt:(window.event?window.event:null);//兼容IE和FF
    if (evted.keyCode==13){
        search_query(q);
    }
}

