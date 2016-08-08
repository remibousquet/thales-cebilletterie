(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('EnfantDeleteController',EnfantDeleteController);

    EnfantDeleteController.$inject = ['$uibModalInstance', 'entity', 'Enfant'];

    function EnfantDeleteController($uibModalInstance, entity, Enfant) {
        var vm = this;

        vm.enfant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Enfant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
