if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    };
}
var searchQuery = function(){
    var query = $("input[name='searchbar']").val();
    query = query.trim();
    window.location.href="s?q="+query;
};
function enterIn(evt){
    var evt=evt?evt:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        searchQuery();
    }
}
