(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('DemandesController', DemandesController);

    DemandesController.$inject = ['$scope', '$state', 'Demande', 'AlertService'];

    function DemandesController ($scope, $state, Demande, AlertService) {
        var vm = this;
        
    }
})();
