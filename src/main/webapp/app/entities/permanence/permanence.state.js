(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('permanence', {
            parent: 'entity',
            url: '/permanence',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.permanence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/permanence/permanences.html',
                    controller: 'PermanenceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('permanence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('permanence-detail', {
            parent: 'entity',
            url: '/permanence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.permanence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/permanence/permanence-detail.html',
                    controller: 'PermanenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('permanence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Permanence', function($stateParams, Permanence) {
                    return Permanence.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'permanence',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('permanence-detail.edit', {
            parent: 'permanence-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/permanence/permanence-dialog.html',
                    controller: 'PermanenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Permanence', function(Permanence) {
                            return Permanence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('permanence.new', {
            parent: 'permanence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/permanence/permanence-dialog.html',
                    controller: 'PermanenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                horaire: null,
                                message: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('permanence', null, { reload: true });
                }, function() {
                    $state.go('permanence');
                });
            }]
        })
        .state('permanence.edit', {
            parent: 'permanence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/permanence/permanence-dialog.html',
                    controller: 'PermanenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Permanence', function(Permanence) {
                            return Permanence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('permanence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('permanence.delete', {
            parent: 'permanence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/permanence/permanence-delete-dialog.html',
                    controller: 'PermanenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Permanence', function(Permanence) {
                            return Permanence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('permanence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
