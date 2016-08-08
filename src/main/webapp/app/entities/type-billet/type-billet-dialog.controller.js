(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TypeBilletDialogController', TypeBilletDialogController);

    TypeBilletDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeBillet'];

    function TypeBilletDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeBillet) {
        var vm = this;

        vm.typeBillet = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeBillet.id !== null) {
                TypeBillet.update(vm.typeBillet, onSaveSuccess, onSaveError);
            } else {
                TypeBillet.save(vm.typeBillet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:typeBilletUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
