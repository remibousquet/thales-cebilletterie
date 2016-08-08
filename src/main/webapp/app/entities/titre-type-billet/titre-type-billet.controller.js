(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TitreTypeBilletController', TitreTypeBilletController);

    TitreTypeBilletController.$inject = ['$scope', '$state', 'TitreTypeBillet', 'TitreTypeBilletSearch'];

    function TitreTypeBilletController ($scope, $state, TitreTypeBillet, TitreTypeBilletSearch) {
        var vm = this;
        
        vm.titreTypeBillets = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TitreTypeBillet.query(function(result) {
                vm.titreTypeBillets = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TitreTypeBilletSearch.query({query: vm.searchQuery}, function(result) {
                vm.titreTypeBillets = result;
            });
        }    }
})();
