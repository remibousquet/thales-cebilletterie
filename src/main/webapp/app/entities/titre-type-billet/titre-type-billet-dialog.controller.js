(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('TitreTypeBilletDialogController', TitreTypeBilletDialogController);

    TitreTypeBilletDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TitreTypeBillet'];

    function TitreTypeBilletDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TitreTypeBillet) {
        var vm = this;

        vm.titreTypeBillet = entity;
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
            if (vm.titreTypeBillet.id !== null) {
                TitreTypeBillet.update(vm.titreTypeBillet, onSaveSuccess, onSaveError);
            } else {
                TitreTypeBillet.save(vm.titreTypeBillet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:titreTypeBilletUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
