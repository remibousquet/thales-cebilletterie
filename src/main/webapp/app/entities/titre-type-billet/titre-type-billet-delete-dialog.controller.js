(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TitreTypeBilletDeleteController',TitreTypeBilletDeleteController);

    TitreTypeBilletDeleteController.$inject = ['$uibModalInstance', 'entity', 'TitreTypeBillet'];

    function TitreTypeBilletDeleteController($uibModalInstance, entity, TitreTypeBillet) {
        var vm = this;

        vm.titreTypeBillet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TitreTypeBillet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
