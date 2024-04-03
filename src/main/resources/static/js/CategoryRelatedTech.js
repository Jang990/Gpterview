let beforeRelatedTechIds = [];
$(function(){
    categoryOptionForm.change(function() {
      loadRelatedTech(this.value);
    });
});

function loadRelatedTech(categoryId) {
    $.get(`/api/job/category/${categoryId}/tech`, function(relatedTech) {
        $.each(relatedTech, function(index, tech) {
            tagify.addTags({"value": tech.id, "name": tech.name});
            tagify.removeTag(beforeRelatedTechIds);
            beforeRelatedTechIds.push(tech.id);
        });
    });
    console.log(beforeRelatedTechIds);
}