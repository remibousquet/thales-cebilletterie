(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('BilletDialogController', BilletDialogController);

    BilletDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Billet', 'TypeBillet', 'TitreTypeBillet'];

    function BilletDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Billet, TypeBillet, TitreTypeBillet) {
        var vm = this;

        vm.billet = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.typebillets = TypeBillet.query();
        vm.titretypebillets = TitreTypeBillet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.billet.id !== null) {
                Billet.update(vm.billet, onSaveSuccess, onSaveError);
            } else {
                Billet.save(vm.billet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cebilletterieApp:billetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDebut = false;
        vm.datePickerOpenStatus.dateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
