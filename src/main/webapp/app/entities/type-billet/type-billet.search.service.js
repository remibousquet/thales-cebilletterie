(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('TypeBilletSearch', TypeBilletSearch);

    TypeBilletSearch.$inject = ['$resource'];

    function TypeBilletSearch($resource) {
        var resourceUrl =  'api/_search/type-billets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
