(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeBilletDeleteController',TypeBilletDeleteController);

    TypeBilletDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeBillet'];

    function TypeBilletDeleteController($uibModalInstance, entity, TypeBillet) {
        var vm = this;

        vm.typeBillet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeBillet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
