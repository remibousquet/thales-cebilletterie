(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-demande', {
            parent: 'entity',
            url: '/type-demande',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.typeDemande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-demande/type-demandes.html',
                    controller: 'TypeDemandeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeDemande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-demande-detail', {
            parent: 'entity',
            url: '/type-demande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.typeDemande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-demande/type-demande-detail.html',
                    controller: 'TypeDemandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeDemande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeDemande', function($stateParams, TypeDemande) {
                    return TypeDemande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-demande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-demande-detail.edit', {
            parent: 'type-demande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-demande/type-demande-dialog.html',
                    controller: 'TypeDemandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeDemande', function(TypeDemande) {
                            return TypeDemande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-demande.new', {
            parent: 'type-demande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-demande/type-demande-dialog.html',
                    controller: 'TypeDemandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-demande', null, { reload: true });
                }, function() {
                    $state.go('type-demande');
                });
            }]
        })
        .state('type-demande.edit', {
            parent: 'type-demande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-demande/type-demande-dialog.html',
                    controller: 'TypeDemandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeDemande', function(TypeDemande) {
                            return TypeDemande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-demande', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-demande.delete', {
            parent: 'type-demande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-demande/type-demande-delete-dialog.html',
                    controller: 'TypeDemandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeDemande', function(TypeDemande) {
                            return TypeDemande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-demande', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
