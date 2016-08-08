(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PaiementDeleteController',PaiementDeleteController);

    PaiementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Paiement'];

    function PaiementDeleteController($uibModalInstance, entity, Paiement) {
        var vm = this;

        vm.paiement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Paiement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
