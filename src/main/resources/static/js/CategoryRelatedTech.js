$(function(){
    categoryOptionForm.change(function() {
      loadRelatedTech(this.value);
    });
});

function loadRelatedTech(categoryId) {
    tagify.removeAllTags();
    $.get(`/api/job/category/${categoryId}/tech`, function(relatedTech) {
        let tags = [];
        $.each(relatedTech, function(index, tech) {
            tags.push({"value": tech.name, "id": tech.id});
        });
        tagify.addTags(tags);
    });
}