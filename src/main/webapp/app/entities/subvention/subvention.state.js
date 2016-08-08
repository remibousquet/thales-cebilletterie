(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subvention', {
            parent: 'entity',
            url: '/subvention',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.subvention.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subvention/subventions.html',
                    controller: 'SubventionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subvention');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('subvention-detail', {
            parent: 'entity',
            url: '/subvention/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cebilletterieApp.subvention.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subvention/subvention-detail.html',
                    controller: 'SubventionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subvention');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Subvention', function($stateParams, Subvention) {
                    return Subvention.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'subvention',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('subvention-detail.edit', {
            parent: 'subvention-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subvention/subvention-dialog.html',
                    controller: 'SubventionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subvention', function(Subvention) {
                            return Subvention.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subvention.new', {
            parent: 'subvention',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subvention/subvention-dialog.html',
                    controller: 'SubventionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subventionAnnee: null,
                                subventionMontant: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subvention', null, { reload: true });
                }, function() {
                    $state.go('subvention');
                });
            }]
        })
        .state('subvention.edit', {
            parent: 'subvention',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subvention/subvention-dialog.html',
                    controller: 'SubventionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subvention', function(Subvention) {
                            return Subvention.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subvention', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subvention.delete', {
            parent: 'subvention',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subvention/subvention-delete-dialog.html',
                    controller: 'SubventionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Subvention', function(Subvention) {
                            return Subvention.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subvention', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
